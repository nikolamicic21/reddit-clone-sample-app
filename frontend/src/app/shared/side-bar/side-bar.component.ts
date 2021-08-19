import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss'],
})
export class SideBarComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  goToCreatePost(): void {
    this.router.navigate(['/create-post']);
  }

  goToCreateSubreddit(): void {
    this.router.navigate(['/create-subreddit']);
  }
}
