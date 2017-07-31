import {Component} from '@angular/core';
import { NavController, MenuController } from 'ionic-angular';

declare var someGlobal;

@Component({
  selector: 'no-internet',
  template: `
  
    <ion-content class="home-content">
        <ion-list class="no-internet">
        <ion-icon name="cloud"></ion-icon>
            <br>NO INTERNET CONNECTION
        </ion-list>
    </ion-content>
  `
})

export class NoInternetComponent {

  title: string = "No Internet";

  constructor(public menuCtrl: MenuController,
              private navCtrl: NavController) {
      this.menuCtrl.enable(false);
  }
}