import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SessionService } from '../../services/session-service/session.service';
import { ISession } from '../models/session.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-delete-session',
  templateUrl: './delete-session.component.html',
  styleUrls: ['./delete-session.component.scss']
})
export class DeleteSessionComponent {
  constructor(
    public _dialogRef: MatDialogRef<DeleteSessionComponent>,
    @Inject(MAT_DIALOG_DATA) public session: ISession,
    private sessionService: SessionService,
    private _toastrService:ToastrService
  ) { }

  onCancelClick(): void {
    this._dialogRef.close();
  }

  deleteSession() {
    this.sessionService.deleteSession(this.session.sessionId).subscribe(
      (response) => {
        this._toastrService.success(
               response.message,
               'Success'
             );
        this._dialogRef.close({ deleted: true });
      },
      (error) => {
        this._dialogRef.close({ deleted: false });
      }
    )
  }
}
