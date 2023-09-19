import { Injectable } from '@angular/core';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private _isLoggedIn = false;
  private _userName = '';

  get isLoggedIn(): boolean {
    return this._isLoggedIn;
  }

  get username(): string {
    return this._userName;
  }

  login(username: string, password: string): boolean {
    if (username === 'admin' && password === 'admin123') {
      this._isLoggedIn = true;
      this._userName = username;
      return true;
    } else {
      return false;
    }
  }

  logout(): void {
    this._isLoggedIn = false;
    this._userName = '';
  }
}
