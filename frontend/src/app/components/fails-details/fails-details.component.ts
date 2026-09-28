import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';

import { ArrayDataSource } from '@angular/cdk/collections';
import { FlatTreeControl, CdkTreeModule } from '@angular/cdk/tree';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { UserService } from '../../services/user/user.service';
import { FailsService } from '../../services/fails/fails.service';
import { LikesService } from '../../services/likes/likes.service';
import { CommentsService } from '../../services/comments/comments.service';
import { User } from '../../dtos/user/user';
import { Fail } from '../../dtos/fails/fail';
import { Image } from '../../dtos/image/image';
import { CommentCreate } from '../../dtos/comments/comment-create';
import { NotificationService } from '../../services/notification/notification.service';

/** Flat node with expandable and level information */
interface ExampleFlatNode {
  expandable: boolean;
  message: string;
  userName: string;
  created: Date;
  level: number;
  isExpanded?: boolean;
}

@Component({
  selector: 'app-fails-details',
  templateUrl: './fails-details.component.html',
  styleUrls: ['./fails-details.component.css']
})
export class FailsDetailsComponent {

  error = false;
  errorMessage = '';

  currUser: User;
  id: number;
  fail: Fail;
  name: string;
  description: string;
  date: Date;
  mainImage: string;
  childImages: Array<{ id: number, content: string }> = [];
  likes: number;
  likedByUser: boolean;
  commentForm: FormGroup;
  commentTree: ExampleFlatNode[] = [];
  showClick: boolean;

  dataSource: any;
  //dataSource = new ArrayDataSource(TREE_DATA);

  constructor(private aRoute: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private userService: UserService,
              private failsService: FailsService,
              private likesService: LikesService,
              private commentsService: CommentsService,
              private location: Location,
              private notificationService: NotificationService) {
    this.commentForm = this.formBuilder.group({
      message: ['', []]
    });
  }

   ngOnInit() {
    this.aRoute.params.subscribe(data => {
      this.id = data['id'];
    });
    console.log('id' + this.id);
    this.loadUser();
    this.loadFail(this.id);
    this.loadCommentTree(this.id);
    this.showClick = false;
    }

  loadCommentTree(id: number) {
    this.commentsService.getCommentsById(id).subscribe({
      next: data => {
        console.log('Get comment tree : ', data);
        this.commentTree = [];
        for (const child of data) {
          this.populateTree(child, 0);
        }
        this.dataSource = new ArrayDataSource(this.commentTree);
      },
      error: error => {
        console.error('Error getting comment tree', error);
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  private populateTree(comment: any, level: number) {
    //let isExpanded = level === 0 ? true : false;
    this.commentTree.push({
      'expandable': comment.children != null,
      'message': comment.message,
      'userName': comment.userName,
      'created': comment.created,
      'level': level,
      'isExpanded': true,
    });
    console.log('Tree', JSON.stringify(this.commentTree));
    console.log('Tree populating', comment);
    if (comment.children != null) {
      for (const child of comment.children) {
        this.populateTree(child, level + 1);
        console.log('Tree populating children', child);
      }
    }
  }

  treeControl = new FlatTreeControl<ExampleFlatNode>(
      node => node.level,
      node => node.expandable,
  );

  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;

  getParentNode(node: ExampleFlatNode) {
    const nodeIndex = this.commentTree.indexOf(node);
    for (let i = nodeIndex - 1; i >= 0; i--) {
      if (this.commentTree[i].level === node.level - 1) {
        return this.commentTree[i];
      }
    }
    return null;
  }

  shouldRender(node: ExampleFlatNode) {
    let parent = this.getParentNode(node);
    while (parent) {
      if (!parent.isExpanded) {
        return false;
      }
      parent = this.getParentNode(parent);
    }
    return true;
  }

  private loadUser() {
    this.userService.getCurrentUserDetails().subscribe({
      next: user => {
        this.currUser = user;
      },
      error: error => {
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  private loadFail(id: number) {
    this.failsService.getFailsById(id).subscribe({
      next: (fail: Fail) => {
        this.fail = fail;
        this.name = fail.name;
        this.description = fail.description;
        this.date = new Date(this.fail.date);
        this.childImages = [];
        this.mainImage = 'data:image/png;base64,' + fail.mainImage.content;
        this.childImages.push({ id: fail.mainImage.id, content: this.mainImage });
        for (const img of fail.images) {
          this.childImages.push({ id: img.id, content: 'data:image/png;base64,' + img.content });
        }
        this.likes = fail.likes;
        this.likedByUser = fail.likedByUser;
      },
      error: error => /* this.nService.notifyUserOnError(error.errorMessage, error.errors) */ {console.log(error.errorMessage)}
    });
  }

  likeFail() {
    this.likesService.likeFail(this.id).subscribe({
      next: () => {
        console.log('Successfully liked fail with id: ' + this.id);
        this.loadFail(this.id);
        this.router.navigate(['/fails/' + this.id]);
      },
      error: (error: any) => {
        this.error = true;
        //console.log(JSON.stringify(error));
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = error.error;
        }
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  unlikeFail() {
    this.likesService.unlikeFail(this.id).subscribe({
      next: () => {
        console.log('Successfully unliked fail with id: ' + this.id);
        this.loadFail(this.id);
        this.router.navigate(['/fails/' + this.id]);
      },
      error: (error: any) => {
        this.error = true;
        //console.log(JSON.stringify(error));
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.message;
        } else {
          this.errorMessage = error.error;
        }
        //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
      }
    });
  }

  showComments() {
    this.showClick = !this.showClick;
  }

  commentFail() {
    if (this.commentTree !== null) {
      let newComment = new CommentCreate(this.commentForm.controls['message'].value, 0, this.id);
      if (this.commentTree.length === 0) {
        //newComment.setParrentId(0);
      }

      this.commentsService.createComment(newComment).subscribe({
        next: () => {
          this.loadFail(this.id);
          this.loadCommentTree(this.id);
          this.router.navigate(['/fails/' + this.id]);
        },
        error: (error: any) => {
          this.error = true;
          //console.log(JSON.stringify(error));
          if (typeof error.error === 'object') {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = error.error;
          }
          //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
        }
      });
    }
  }

  loadPdf() {
    this.failsService.getFailPDF(this.id).subscribe(
    response => {
      const file = new Blob([response], {type: 'application/pdf'});
      const fileURL = URL.createObjectURL(file);
      window.open(fileURL);
    },
    error => {
      this.error = true;
      if (typeof error.error === 'object') {
        this.errorMessage = error.error.error;
      } else {
        this.errorMessage = error.error;
      }
      //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
    });
  }

  focus(imageId: number) {
    this.mainImage = this.childImages.filter(f => f.id == imageId)[0].content;
  }

  goBack(): void {
    this.location.back();
  }

}
