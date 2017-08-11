import {Component} from '@angular/core';
import { Push, PushObject, PushOptions } from '@ionic-native/push';

@Component({
    selector: 'push-notification'
})


export class PushNotification {

    constructor(public push : Push){

  this.push.hasPermission()
   .then((res: any) => {

    if(res.isEnabled) {
       console.log('We have permission to send push notifications');
       } else{
       console.log('We do not have permission to send push notifications');
      }
    });
   this.registerDevice();
    }



    registerDevice(){
        const options: PushOptions = {
        android: {
             senderID: '12345679'
            }
        } 
    const pushObject: PushObject = this.push.init(options);
    pushObject.on('registration').subscribe((registration: any) => console.log('Device registered', registration));
    }

  

    

}


