import {Injectable} from '@angular/core';
import {UserRegister} from '../../dtos/user/userRegister';
import {Observable, switchMap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../../global/globals';
import {User} from '../../dtos/user/user';
//import {News} from '../../dtos/news/news';
import {ForgotPasswordDto} from '../../dtos/common/ForgotPasswordDto';
import {ResetPasswordDto} from '../../dtos/user/ResetPasswordDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private authBaseUri: string = this.globals.backendUrl + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Create new user. Returns the new registered user.
   *
   * @param user UserRegister data
   */
  registerUser(user: UserRegister): Observable<UserRegister> {
    let userDtoRegister: Observable<UserRegister>;
    userDtoRegister = this.httpClient.post<UserRegister>(this.authBaseUri, user);
    console.log(userDtoRegister);
    return userDtoRegister;
  }

  registerUserNoImg(user: UserRegister): Observable<UserRegister> {
    console.log('Create user with nickname ' + user.nickname);
    return this.httpClient.post<UserRegister>(this.authBaseUri, user);
  }

  forgotPassword(forgotPasswordDto: ForgotPasswordDto): Observable<void> {
    console.log('Send password Reset Email to ' + forgotPasswordDto.email);
    return this.httpClient.post<void>(this.authBaseUri + '/forgot-password', forgotPasswordDto);
  }

  resetPassword(resetPasswordDto: ResetPasswordDto): Observable<void> {
    console.log('Reset password');
    return this.httpClient.post<void>(this.authBaseUri + '/reset-password', resetPasswordDto);
  }

  updateUser(user: UserRegister): Observable<User> {
    return this.httpClient.put<User>(this.authBaseUri, user);
  }

  getCurrentUserDetails(): Observable<User> {
    return this.httpClient.get<User>(this.authBaseUri);
  }

  deleteUser(): Observable<void> {
    return this.httpClient.delete<void>(this.authBaseUri);
  }
}
