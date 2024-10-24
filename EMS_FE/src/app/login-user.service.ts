import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from './login-response';

@Injectable({
  providedIn: 'root'
})
export class LoginUserService {
  private baseUrl = "http://localhost:8090/user/login";

  constructor(private httpClient: HttpClient) {

  }

  loginUser(user: User): Observable<LoginResponse> {
    console.log(user);
    const headers = new HttpHeaders()
      .set('content-type', 'application/json')
      .set('Access-Control-Allow-Origin', '*');
    return this.httpClient.post(`${this.baseUrl}`, user, {
      headers: headers
    }).pipe(tap( (resp: any) => {
                localStorage.setItem('accessToken',  resp.accessToken);
                localStorage.setItem('userName', resp.user.name);
                localStorage.setItem('userRole', resp.user.roles);
                }));
  }
}
