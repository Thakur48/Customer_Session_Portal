import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { SessionService } from '../../services/session-service/session.service';
import { ICreateSessionDto, ISession } from '../models/session.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-session',
  templateUrl: './new-session.component.html',
  styleUrls: ['./new-session.component.scss'],
})
export class NewSessionComponent {
  @Output() sessionCreated = new EventEmitter<ISession>();
  isLoading = false;
  errorMessage = '';

  constructor(
    private popup: MatDialogRef<NewSessionComponent>,
    private fb: FormBuilder,
    private sessionservice: SessionService,
    private route:Router
  ) {}

  createSessionForm = this.fb.group({
    customerId: ['', [Validators.required]],
    sessionName: ['', [Validators.required]],
    remark: ['', [Validators.required]],
  });
  get customerId() {
    return this.createSessionForm.get('customerId');
  }

  get sessionName() {
    return this.createSessionForm.get('sessionName');
  }
  get remark() {
    return this.createSessionForm.get('remark');
  }

  createSession() {
    this.isLoading = true;
    localStorage.setItem('RMname','RM1')
    const createDto : ICreateSessionDto={
      sessionName:this.createSessionForm.value.sessionName||'',
      customerId:this.createSessionForm.value.customerId||'',
      createdBy:localStorage.getItem('RMname') ||'',
      remark:this.createSessionForm.value.remark||''
    }
    this.sessionservice.createSession(createDto).subscribe(

      () => {
        this.route.navigateByUrl('/home');
        this.closeModal();
      },
      (error) => {
        this.errorMessage = 'Failed to create the session!';
        this.isLoading = false;
      }
    );
  }
  closeModal() {
    this.popup.close();
  }
  onClose() {
    this.popup.close();
  }
}
