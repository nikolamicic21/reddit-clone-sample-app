import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SubRedditResponseModel } from './sub-reddit-response.model';
import { Observable } from 'rxjs';
import { CreateSubRedditRequestModel } from './create-sub-reddit/create-sub-reddit-request.model';

@Injectable({
  providedIn: 'root',
})
export class SubRedditService {
  constructor(private httpClient: HttpClient) {}

  getAll(): Observable<SubRedditResponseModel[]> {
    return this.httpClient.get<SubRedditResponseModel[]>(
      'http://localhost:8080/api/subreddits'
    );
  }

  create(
    request: CreateSubRedditRequestModel
  ): Observable<SubRedditResponseModel> {
    return this.httpClient.post<SubRedditResponseModel>(
      'http://localhost:8080/api/subreddits',
      request
    );
  }
}
