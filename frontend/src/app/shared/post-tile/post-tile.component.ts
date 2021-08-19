import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { PostResponseModel } from '../post-response.model';
import { PostService } from '../post.service';
import { faComments } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.scss'],
})
export class PostTileComponent implements OnInit {
  faComments = faComments;
  @Input() posts: PostResponseModel[] | undefined;

  constructor(private router: Router) {}

  ngOnInit(): void {}

  goToPost(name: string): void {
    this.router.navigateByUrl(`/view-post/${name}`);
  }
}
