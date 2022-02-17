import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  registerForm = this.fb.group({
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(254),
        Validators.email,
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    email: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(254),
        Validators.email,
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],

    // Agregar campos nuevos
    documentType: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(10)]],

    documentNumber: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],

    firstName: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30), Validators.pattern('')]],
    lastName: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30), Validators.pattern('')]],

    password: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(50),
        Validators.pattern(/\d/),
        Validators.pattern(/[A-Z]/),
        Validators.pattern(/[a-z]/),
      ],
    ],
    confirmPassword: [null, Validators.minLength(5), Validators.maxLength(50), Validators.compose([Validators.required])],
  });

  constructor(private translateService: TranslateService, private registerService: RegisterService, private fb: FormBuilder) {}

  ngAfterViewInit(): void {
    if (this.login) {
      this.login.nativeElement.focus();
    }
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const firstName = this.registerForm.get(['firstName'])!.value;
    const lastName = this.registerForm.get(['lastName'])!.value;
    const password = this.registerForm.get(['password'])!.value;
    const documentType = this.registerForm.get(['documentType'])!.value;
    const documentNumber = this.registerForm.get(['documentNumber'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const email = this.registerForm.get(['email'])!.value;
      const login = email;

      this.registerService
        .save({ login, email, password, documentNumber, documentType, firstName, lastName, langKey: this.translateService.currentLang })
        .subscribe(
          () => (this.success = true),
          response => this.processError(response)
        );
    }
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }
}
