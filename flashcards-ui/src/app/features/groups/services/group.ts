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

  getAll(): Observable<GroupResponse[]> {
    return this.http.get<GroupResponse[]>(this.apiUrl);
  }

  getById(id: string): Observable<GroupResponse> {
    return this.http.get<GroupResponse>(`${this.apiUrl}/${id}`);
  }

  update(id: string, request: GroupRequest): Observable<GroupResponse> {
    return this.http.put<GroupResponse>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
