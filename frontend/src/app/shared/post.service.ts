import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostResponseModel } from './post-response.model';
import { CreatePostRequestModel } from '../post/create-post/create-post-request.model';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private httpClient: HttpClient) {}

  getAll(): Observable<PostResponseModel[]> {
    return this.httpClient.get<PostResponseModel[]>(
      'http://localhost:8080/api/posts'
    );
  }

  create(request: CreatePostRequestModel): Observable<PostResponseModel> {
    return this.httpClient.post<PostResponseModel>(
      'http://localhost:8080/api/posts',
      request
    );
  }

  getByName(name: string): Observable<PostResponseModel> {
    return this.httpClient.get<PostResponseModel>(
      `http://localhost:8080/api/posts/${name}`
    );
  }

  getByUserName(username: string): Observable<PostResponseModel[]> {
    return this.httpClient.get<PostResponseModel[]>(
      `http://localhost:8080/api/posts/users/${username}`
    );
  }
}
