import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateVoteModel } from './create-vote-request.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VoteService {
  constructor(private httpClient: HttpClient) {}

  create(request: CreateVoteModel): Observable<CreateVoteModel> {
    return this.httpClient.post<CreateVoteModel>(
      'http://localhost:8080/api/posts',
      request
    );
  }
}
