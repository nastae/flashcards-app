import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GroupRequest } from '../models/group-request';
import { Observable } from 'rxjs';
import { GroupResponse } from '../models/group-response';

@Injectable({
  providedIn: 'root',
})
export class Group {

  private apiUrl = "http://localhost:8080/api/groups"

  constructor(private http: HttpClient) {}

  createGroup(request: GroupRequest): Observable<GroupResponse> {
    return this.http.post<GroupResponse>(this.apiUrl, request);
  }
}
