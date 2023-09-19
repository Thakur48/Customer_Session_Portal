import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IApiResponses, ICreateSessionDto, ISession } from '../../features/models/session.model';
import { IUpdateSessionDto } from '../../features/models/session.model';
@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private sessions = 'http://localhost:8083/sessions';
  constructor(private http: HttpClient) {}

  getSessions(
    sessionStatus: string,
    offset: number,
    pageSize: number
  ): Observable<IApiResponses> {
    const url = `${this.sessions}/${sessionStatus}`;
    const params = {
      pageNumber: offset.toString(),
      pageSize: pageSize.toString(),
      sortBy: 'sessionName',
      sortDir: 'asc',
    };
    const headers = new HttpHeaders({
      'RelationshipManagerID': 'RM1',
    });

    // Make the GET request with headers and query parameters
    return this.http.get<IApiResponses>(url, { headers, params });
  }

  updateSession(
    sessionID: string,
    updateSessionDto: IUpdateSessionDto
  ): Observable<ISession> {
    const url = `${this.sessions}/${sessionID}`;

    const requestPayload = {
      remark: updateSessionDto.remark,
      sessionId: updateSessionDto.sessionId,
      sessionName: updateSessionDto.sessionName
    };

    return this.http.put<ISession>(url, requestPayload);
  }


  createSession(sessionData: ICreateSessionDto): Observable<ISession> {
    const url = `${this.sessions}`;
    return this.http.post<ISession>(url, sessionData);
  }

  deleteSession(sessionID: string): Observable<any> {
    const url = `${this.sessions}/${sessionID}`;
    const body = { "sessionId": sessionID }; // Create a JSON object with the sessionId

    return this.http.delete(url, { body });
  }

  archiveSession(sessionId: string): Observable<any> {
    const url = `${this.sessions}/archive/${sessionId}`;
    const body = { sessionId }; // Assuming the backend expects a structure like { "sessionId": "session_id_value" }
    return this.http.patch(url, body);
  }

}
