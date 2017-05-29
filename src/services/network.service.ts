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
  console.log('network was disconnected :-(');
});
 
// stop disconnect watch
disconnectSubscription.unsubscribe();
}




connect(){


// watch network for a connection
let connectSubscription = this.network.onConnect().subscribe(() => {
  console.log('network connected!');
  // We just got a connection but we need to wait briefly
   // before we determine the connection type. Might need to wait.
  // prior to doing any api requests as well.
  setTimeout(() => {
    if (this.network.type === 'wifi') {
      console.log('we got a wifi connection, woohoo!');
    }
  }, 3000);
});

// stop connect watch
connectSubscription.unsubscribe();
}
}