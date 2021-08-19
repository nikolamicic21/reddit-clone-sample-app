import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateCommentRequestModel } from './create-comment-request.model';
import { Observable } from 'rxjs';
import { CommentResponseModel } from './comment-response.model';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  constructor(private httpClient: HttpClient) {}

  create(request: CreateCommentRequestModel): Observable<CommentResponseModel> {
    return this.httpClient.post<CommentResponseModel>(
      'http://localhost:8080/api/comments',
      request
    );
  }

  getByPostName(postName: string): Observable<CommentResponseModel[]> {
    return this.httpClient.get<CommentResponseModel[]>(
      `http://localhost:8080/api/comments/posts/${postName}`
    );
  }

  getByUserName(username: string): Observable<CommentResponseModel[]> {
    return this.httpClient.get<CommentResponseModel[]>(
      `http://localhost:8080/api/comments/users/${username}`
    );
  }
}
