import { Injectable } from '@angular/core';
import { Network } from '@ionic-native/network';
import { Platform } from 'ionic-angular';


declare var Connection;
 
@Injectable()
export class NetworkService {
 
  onDevice: boolean;
 
  constructor(public platform: Platform,
              public network : Network){
    this.onDevice = this.platform.is('cordova');
  }

  disconnect(){ 
    let disconnectSubscription = this.network.onDisconnect().subscribe(() => {
      alert('network was disconnected :-(');
    });
 
     disconnectSubscription.unsubscribe();
  }

  connect(){
      let connectSubscription = this.network.onConnect().subscribe(() => {
      alert('network connected!');
      setTimeout(() => {
        if (this.network.type === 'wifi') {
          alert('we got a wifi connection, woohoo!');
        }
      }, 3000);
  });

    //connectSubscription.unsubscribe();
  }
}