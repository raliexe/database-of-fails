import { Component } from '@angular/core';
//import { Component, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { DatePipe } from '@angular/common';
import { NotificationService } from '../../services/notification/notification.service';

import { FailsService } from '../../services/fails/fails.service';
import { FailCreate } from '../../dtos/fails/fail-create';

@Component({
  selector: 'app-fails-edit',
  templateUrl: './fails-edit.component.html',
  styleUrls: ['./fails-edit.component.css']
})
export class FailsEditComponent {

  editForm!: FormGroup;
  updatedFail: FailCreate;
  fail!: FailCreate;
  imageUpload!: File;
  imageContent: string;
  imageName: string;
  imagesUpload: Array<{ id: number, name: string, file: File, content: string, size:number, status: string }> = [];
  //@ViewChild('inputImage') inputImage!: ElementRef<HTMLInputElement>;

  error = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private failsService: FailsService,
    private location: Location,
    private notificationService: NotificationService
  ) {
    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(50)]],
      description: ['', [Validators.required,Validators.maxLength(10000)]],
      date: ['', [Validators.required]],
      image: [],
      childImage: []
    });
  }

  ngOnInit() {
    if (this.activatedRoute.snapshot.params['id'] != undefined && this.editForm != null) {
      this.failsService.getFailsById(this.activatedRoute.snapshot.params['id']).subscribe({
        next: fail => {
          this.editForm.get('name')!.setValue(fail.name);
          this.editForm.get('description')!.setValue(fail.description);
          const datepipe: DatePipe = new DatePipe('en-US')
          let formattedDate = datepipe.transform(fail.date, 'YYYY-MM-dd')
          this.editForm.get('date')!.setValue(formattedDate);

          this.imageContent = 'data:image/png;base64,' + fail.mainImage.content;
          this.imageName = fail.mainImage.fileName;

          for (const img of fail.images) {
            this.imagesUpload.push({ id: img.id,
                                     name: img.fileName,
                                     file: null!,
                                     content: 'data:image/png;base64,' + img.content,
                                     size: 0,
                                     status: 'ACTIVE' });
          }
        },
        error: error => {
          this.error = true;
          if (typeof error.error === 'object') {
            this.errorMessage = error.error.error;
          } else {
            this.errorMessage = error.error;
          }
          //this.notificationService.notifyUserOnError(this.errorMessage, error.errors);
        }
      });
    }
  }

  updateFail() {
    if (this.editForm.valid) {
      this.updatedFail = new FailCreate(
        this.editForm.controls['name'].value,
        this.editForm.controls['description'].value,
        this.editForm.controls['date'].value
      );

      let newFail;
      this.failsService.updateFail(this.updatedFail, this.imageUpload, this.imagesUpload,
                                   this.activatedRoute.snapshot.params['id']).subscribe({
        next: (newFail: any) => {
          console.log('Successfully edited fail with name: ' + newFail.name);
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

  transformImageName(fileName: string): string {
    let MAX_LENGTH = 20;

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
    this.imageName = this.imageUpload.name;

    var reader = new FileReader();
    reader.onload = () => { this.imageContent = "" + reader.result; };
    reader.readAsDataURL(this.imageUpload);
  }

  onChildImageUpload(event: any) {
    for (var i = 0; i < event.target.files.length; i++) {
      var file = event.target.files[i];
      var reader = new FileReader();
      reader.onload = () => {
        this.imagesUpload.push({ id: null!, name: file.name, file: file,
                                 content: "" + reader.result,
                                 size: file.size, status: 'ACTIVE' });
      };
      reader.readAsDataURL(file);
    }
    console.log(this.imagesUpload);
  }

  markForRemove(img: any) {
    img.status = 'DELETED';
  }

  hasInvalidImage() {
    if (this.imageUpload != null && this.imageUpload.size > 1048575) {
      return true;
    }
    if (this.imagesUpload.filter(f => f.status == 'ACTIVE' && f.size > 1048575).length > 0) {
      return true;
    }
    return false;
  }

  goBack(): void {
    this.location.back();
  }

}
