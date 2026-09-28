import {Injectable} from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor() { }

  public createMixinNotificationPopup() {
    return Swal.mixin({
      customClass: {
        cancelButton: 'btn btn-success',
        confirmButton: 'btn btn-danger'
      },
      buttonsStyling: true
    });
  }

  public notifyUserOnError(errorMessage: string, errors: object, time?: number) {
    let errorsString: string;
    const errorTitle = 'Error';
    if (errorMessage != null && errorMessage.includes('[') && errorMessage.includes(']')) {
      errorMessage = errorMessage.substring(errorMessage.indexOf('[') + 1, errorMessage.indexOf(']'));
    }
    if (errorMessage != null && errors != null) {
      errorsString = errors.toString().replace(',', '<br>');
      if (time === undefined) {
        Swal.fire(errorMessage, errorsString, 'error');
      } else {
        Swal.fire({
          icon: 'error',
          title: errorMessage,
          text: errorsString,
          timer: time,
          showCancelButton: false,
          showConfirmButton: false
        });
      }
    } else if (errorMessage != null && errors == null) {
      if (time === undefined) {
        Swal.fire(errorTitle, errorMessage, 'error');
      } else {
        Swal.fire({
          icon: 'error',
          title: errorTitle,
          text: errorMessage,
          timer: time,
          showCancelButton: false,
          showConfirmButton: false
        });
      }
    } else if (errorMessage == null && errors != null) {
      errorsString = errors.toString().replace(',', '<br>');
      if (time === undefined) {
        Swal.fire(errorTitle, errorsString, 'error');
      } else {
        Swal.fire({
          icon: 'error',
          title: errorTitle,
          text: errorsString,
          timer: time,
          showCancelButton: false,
          showConfirmButton: false
        });
      }
    } else {
      if (time === undefined) {
        Swal.fire(errorTitle, 'An error occured', 'error');
      } else {
        Swal.fire({
          icon: 'error',
          title: errorTitle,
          text: 'An error occured',
          timer: time,
          showCancelButton: false,
          showConfirmButton: false
        });
      }
    }
  }

  public notifyUserOnSuccess(head: string, message: string, time?: number) {
    if (time === undefined) {
      Swal.fire(head, message, 'success');
    } else {
      Swal.fire({
        icon: 'success',
        title: head,
        text: message,
        timer: time,
        showCancelButton: false,
        showConfirmButton: false
      });
    }
  }

  public notifyUserOnErrorGeneric(errorMessage: string) {
    Swal.fire('Error', errorMessage, 'error');
  }
}
