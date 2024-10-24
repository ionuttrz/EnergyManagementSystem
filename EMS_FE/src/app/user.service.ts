import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from './user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl_user = "http://localhost:8090/user/register";
  constructor(private httpClient: HttpClient) { }

  public addUser(user: User): Observable<User>{
    console.log(user);
    return this.httpClient.post<User>(`${this.baseUrl_user}`, user);
  }
}
