import { Component } from '@angular/core';
//import { Component, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { NotificationService } from '../../services/notification/notification.service';

import { FailsService } from '../../services/fails/fails.service';
import { FailCreate } from '../../dtos/fails/fail-create';

@Component({
  selector: 'app-fails-create',
  templateUrl: './fails-create.component.html',
  styleUrls: ['./fails-create.component.css']
})
export class FailsCreateComponent {

  createForm: FormGroup;
  fail!: FailCreate;
  imageUpload!: File;
  imageContent: string;
  //@ViewChild('inputImage') inputImage!: ElementRef<HTMLInputElement>;

  error = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private failsService: FailsService,
    private location: Location,
    private notificationService: NotificationService
  ) {
    this.createForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(50)]],
      description: ['', [Validators.required,Validators.maxLength(10000)]],
      date: ['', [Validators.required]],
      image: ['', [Validators.required]]
    });
  }

  addFail() {
    if (this.createForm.valid) {
      this.fail = new FailCreate(
        this.createForm.controls['name'].value,
        this.createForm.controls['description'].value,
        this.createForm.controls['date'].value
      );

      let newFail;
      this.failsService.createFail(this.fail, this.imageUpload).subscribe({
        next: (newFail: any) => {
          console.log('Successfully created fail with name: ' + newFail.name);
          this.router.navigate(['/fails']);
        },
        error: (error: any) => {
          this.error = true;
          //console.log(JSON.stringify(error));
          if (typeof error.error === 'object') {
            this.errorMessage = error.error.message;
          } else {
            this.errorMessage = error.error;
          }
          this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
        }
      });
    } else {
      console.error('Invalid input');
    }
  }

  transformImageName(): string {
    const MAX_LENGTH = 17;
    const fileName = this.imageUpload?.name || '';

    if (fileName.length <= MAX_LENGTH) {
      return fileName;
    }

    const extensionIndex = fileName.lastIndexOf('.');
    if (extensionIndex === -1) {
      return fileName.substring(0, MAX_LENGTH) + '...'; // Truncate without extension if extension not found
    }

    const extension = fileName.substring(extensionIndex-3); // Extract extension 5 symbols before.imagetype
    const truncatedName = fileName.substring(0, MAX_LENGTH - extension.length) + '...'; // Truncate name without extension
    return truncatedName + extension; // Combine truncated name and extension
  }

  onImageUpload(event: any) {
    this.imageUpload = event.target.files[0];

    var reader = new FileReader();
    reader.onload = () => { this.imageContent = "" + reader.result; };
    reader.readAsDataURL(this.imageUpload);
  }

  removeImage(inputImage: HTMLInputElement) {
    this.imageUpload = new File([], '');
    this.imageContent = "";
    //inputImage.value = '';

    this.createForm.controls['image'].setValue(null);
  }

  goBack(): void {
    this.location.back();
  }

}
