import {Component} from '@angular/core';
import { NavController, MenuController } from 'ionic-angular';

declare var someGlobal;

@Component({
  selector: 'no-internet',
  template: `
    <ion-header>
      <ion-navbar></ion-navbar>
    </ion-header>
    <ion-content class="csGrayBackground">
        <ion-list class="no-comment">
        <ion-icon name="cloud"></ion-icon>
            <br>NO INTERNET CONNECTION
        </ion-list>
        <button ion-button color="light" icon-left class="csCenter">
            <ion-icon name="refresh"></ion-icon>Tap to retry
        </button>
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