import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { MatTableDataSource } from '@angular/material/table';
import { IApiResponses, ISession } from '../models/session.model';
import { MatDialog } from '@angular/material/dialog';
import { ViewSessionComponent } from '../view-session/view-session.component';
import { NewSessionComponent } from '../new-session/new-session.component';
import { EditSessionComponent } from '../edit-session/edit-session.component';
import { SessionService } from '../../services/session-service/session.service';
import { DeleteSessionComponent } from '../delete-session/delete-session.component';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

  activeDisplayedColumns: string[] = [
    'sessionName',
    'id',
    'customerName',
    'createdBy',
    'createdOn',
    'updatedOn',
    'status',
    'view',
    'edit',
    'delete',
    'archiveFlag',
  ];
  archiveDisplayedColumns: string[] = [
    'sessionName',
    'id',
    'customerName',
    'createdBy',
    'createdOn',
    'updatedOn',
    'status',
    'view',
  ];

  dataSource = new MatTableDataSource<ISession>();
  @ViewChild(MatPaginator)
  paginator!: MatPaginator;
  totalItems = 0;
  pageSizeOptions = [5, 10, 15];
  activeSessionsTab = true;
  archiveSessionsTab = false;
  pageSize = this.pageSizeOptions[0];
  currentPage = 0;
  errorMessage = false;

  constructor(
    private sessionService: SessionService,
    private dialog: MatDialog,
    private cdr: ChangeDetectorRef,
    private _toastrService:ToastrService
  ) {}

  onTabChange(event: MatTabChangeEvent) {
    if (event.index === 1) {
      this.archiveSessionsTab = true;
      this.activeSessionsTab = false;
    } else {
      this.activeSessionsTab = true;
      this.archiveSessionsTab = false;
    }
    this.totalItems = 0;
    this.currentPage = 0;
    this.paginator.pageIndex = 0;
    this.getData();
  }

  ngOnInit(): void {
    this.getData();
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.getData();
  }

  getData() {
    this.dataSource.paginator = null;
    const sessionStatus = this.activeSessionsTab === true ? 'A' : 'X';
    const offset = this.currentPage;
    this.sessionService
      .getSessions(sessionStatus, offset, this.pageSize)
      .subscribe(
        (response: IApiResponses) => {
          console.log(response)
          this.dataSource.data = response.content;
          this.totalItems = response.totalElements;
        },
        (error: Error) => {
          this.errorMessage = true;
        }
      );
  }

  editSession(session: ISession) {
    const dialogRef = this.dialog.open(EditSessionComponent, {
      width: '35%',
      data: session,
    });
    dialogRef.componentInstance.sessionUpdated.subscribe((updatedSession: ISession) => {
      const sessionIndex = this.dataSource.data.findIndex(item => item.sessionId === updatedSession.sessionId);
      if (sessionIndex !== -1) {
        this.dataSource.data[sessionIndex] = updatedSession; // Update the session in the array
        this.dataSource.paginator = this.paginator;
        this.cdr.detectChanges();
      }
    });
    dialogRef.afterClosed().subscribe((updatedSession: ISession) => {
      if (updatedSession) {
        //this.dataSource.data[sessionIndex] = updatedSession;
      }
    });
  }

  deleteSession(session:ISession):void{
    const dialogRef=this.dialog.open(DeleteSessionComponent,{
      width:'35%',
      data:session
    })
    dialogRef.afterClosed().subscribe((result: { deleted: boolean }) => {
      if (result.deleted) {
        const sessionIndex = this.dataSource.data.findIndex(s => s.sessionId === session.sessionId);
        if (sessionIndex !== -1) {
          this.dataSource.data.splice(sessionIndex, 1); // Remove the deleted session
          this.dataSource.paginator = this.paginator;
          this.cdr.detectChanges(); // Trigger change detection
          this.getData();
        }
      }
    });
  }

  archiveSession(session:ISession){
    this.sessionService.archiveSession(session.sessionId).subscribe(
      (response:any)=>{
        console.log("session archived successfully "+session.sessionId);
        this._toastrService.success(
          response.message,
               'Success'
             );
        this.getData();
      },
      (error: any) => {
        console.error('Error archiving session:', error);
      }
    );
  }

  viewSession(session: ISession): void {
    const dialogref = this.dialog.open(ViewSessionComponent, {
      width: '32%',
      height: '60%',
      data: session,
    });
  }

  createSessionDialog() {
    const dialogRef = this.dialog.open(NewSessionComponent, {
      width: '28%',
      height: 'auto',
    });
    dialogRef.componentInstance.sessionCreated.subscribe((newSession: ISession) => {
      this.dataSource.data.unshift(newSession);
      this.totalItems += 1;
      this.paginator.length = this.totalItems;
      this.cdr.detectChanges();
    });

    dialogRef.afterClosed().subscribe((result) => {
    this.cdr.detectChanges();
    this.getData();
    });
  }

  transformSessionID(sessionId: string): string {
    if (sessionId.length >= 2) {
      const prefix = sessionId.slice(0, 10);
      const maskedSuffix = 'XXXX';
      return prefix + maskedSuffix;
    } else {
      return sessionId;
    }
  }
}
