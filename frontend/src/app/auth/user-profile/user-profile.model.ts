import { PostResponseModel } from '../../shared/post-response.model';
import { CommentResponseModel } from '../../comment/comment-response.model';

export interface UserProfileModel {
  posts: PostResponseModel[];
  comments: CommentResponseModel[];
}
