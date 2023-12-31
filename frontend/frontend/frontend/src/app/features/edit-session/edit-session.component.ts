import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ISession } from '../models/session.model';
import { SessionService } from '../../services/session-service/session.service';
import { ToastrService } from 'ngx-toastr';
import { IUpdateSessionDto } from '../models/session.model';

@Component({
  selector: 'app-edit-session',
  templateUrl: './edit-session.component.html',
  styleUrls: ['./edit-session.component.scss'],
})
export class EditSessionComponent implements OnInit {
  @Output() sessionUpdated = new EventEmitter<ISession>();
  updateButtonDisabled = true;
  editForm!: FormGroup;

  constructor(
    private _formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public session: ISession,
    private _dialogRef: MatDialogRef<EditSessionComponent>,
    private _toastrService: ToastrService,
    private _sessionService: SessionService
  ) {
    this.editForm = this._formBuilder.group({
      customerName: new FormControl({
        value: this.session.customerName,
        disabled: true,
      }),
      sessionName: new FormControl(this.session.sessionName, [
        Validators.required,
        Validators.minLength(3),
      ]),
      notes: new FormControl(this.session.remark, [
        Validators.required,
        Validators.minLength(3),
      ]),
    });
  }

  ngOnInit(): void {
    this.editForm.valueChanges.subscribe((value) => {
      this.updateButtonDisabled =
        JSON.stringify(value) === JSON.stringify(this.session);
    });
  }

  onEditFormSubmit() {
    if (this.editForm.valid) {
      const updateDto: IUpdateSessionDto = {
        sessionName: this.editForm.value.sessionName,
        customerName: this.session.customerName,
        remark: this.editForm.value.notes,
        sessionId: this.session.sessionId,
      };
      this._sessionService
        .updateSession(this.session.sessionId, updateDto)
        .subscribe(
          (updatedSession: ISession) => {
            this.sessionUpdated.emit(updatedSession);
            this._toastrService.success(
                  'Session successfully updated',
                  'Success'
                 );
            this._dialogRef.close();
          },
          (error: any) => {
            console.error('Error updating session:', error);
            if (error.status === 400) {
              this._toastrService.error(error.error.response, 'Error');
              this._dialogRef.close();
            }
          }
        );
    }
  }

  onClose(): void {
    this._dialogRef.close();
  }
}
