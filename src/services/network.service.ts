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
   this.disconnect();
    this.connect();
   
  }

  disconnect(){ 
    let disconnectSubscription = this.network.onDisconnect().subscribe(() => {
      this.checkConnection();
    });
 
     //disconnectSubscription.unsubscribe();
  }

  connect(){
      let connectSubscription = this.network.onConnect().subscribe(() => {
      alert('network connected!');
       this.checkConnection();
  });

    //connectSubscription.unsubscribe();
  }


  public states = {};
  checkConnection(){
    let networkState = this.network.type;
      this.states[Connection.WIFI]   = 'WiFi connection';
      this.states[Connection.CELL_4G] = "4G connection";
      this.states[Connection.CELL_2G] = "Cell 2G connection";
      this.states[Connection.CELL_3G] = "Cell 3G connection";
      this.states[Connection.NONE]     = 'No network connection';
      alert('Connection: ' + this.states[networkState]);
  }
  
  }






