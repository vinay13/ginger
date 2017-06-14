import { Component,Input, Output, EventEmitter } from '@angular/core';
import { NavController } from 'ionic-angular';
// import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage {

  // page1: any = 'page1Component';
  // page2: any = 'page2Component';
  // page3: any = 'page3Component';
  
  constructor(public navCtrl: NavController) {
  }

}
