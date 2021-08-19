import { Component, Input, OnInit } from '@angular/core';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { PostResponseModel } from '../post-response.model';
import { VoteService } from '../vote.service';
import { AuthService } from '../../auth/shared/auth.service';
import { PostService } from '../post.service';
import { ToastrService } from 'ngx-toastr';
import { CreateVoteModel, VoteTypeEnum } from '../create-vote-request.model';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.scss'],
})
export class VoteButtonComponent implements OnInit {
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  @Input() post: PostResponseModel | undefined;

  constructor(
    private voteService: VoteService,
    private authService: AuthService,
    private postService: PostService,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {}

  upVotePost(): void {
    const request: CreateVoteModel = {
      postName: this.post?.name as string,
      type: VoteTypeEnum.UP_VOTE,
    };
    this.createVote(request);
  }

  downVotePost(): void {
    const request: CreateVoteModel = {
      postName: this.post?.name as string,
      type: VoteTypeEnum.DOWN_VOTE,
    };
    this.createVote(request);
  }

  private createVote(request: CreateVoteModel): void {
    this.voteService.create(request).subscribe(
      (response) => {
        this.updateVoteDetails();
      },
      (error) => {
        this.toastrService.error(error.error.message);
        throwError(error);
      }
    );
  }

  private updateVoteDetails(): void {
    this.postService
      .getByName(this.post?.name as string)
      .subscribe((post) => (this.post = post));
  }
}
