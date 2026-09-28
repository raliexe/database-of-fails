import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {UserFilterDto} from '../../dtos/user/UserFilterDto';
import {UserPagedDto} from '../../dtos/user/UserPagedDto';
import {Observable} from 'rxjs';
import {UserRegister} from '../../dtos/user/userRegister';


@Injectable({
  providedIn: 'root'
})
export class ManagementService {

  private managementBaseUri: string = this.globals.backendUrl + '/management';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Gets users from the backend, filtered by different criteria
   *
   * @param userFilterDto DTO containing filter criteria
   */
  getUsersPaged(userFilterDto: UserFilterDto) {
    let params = new HttpParams();
    for (const [key, value] of Object.entries(userFilterDto)) {
      if (value != null) {
        params = params.set(key, value);
      }
    }

    return this.httpClient.get<UserPagedDto>(`${this.managementBaseUri}`, {params});
  }

  /**
   * Locks unlocked users and unlocks locked users.
   *
   * @param id of the user
   */
  changeLockStatus(id: number): Observable<void> {
    let param = new HttpParams();
    param = param.append('id', id);
    console.log(id);
    return this.httpClient.put<void>(this.managementBaseUri + '/status', {}, {params: param});
  }

  /**
   * Reset password if user as admin
   *
   * @param id of the user
   */
  resetUserPassword(id: number): Observable<void> {
    return this.httpClient.put<void>(this.managementBaseUri + '/' + id, {});
  }

  createUser(user: UserRegister): Observable<UserRegister> {
    console.log('Create user with nickname ' + user.nickname);
    return this.httpClient.post<UserRegister>(this.managementBaseUri, user);
  }
}
