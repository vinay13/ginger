import { Component,Input, Output, EventEmitter } from '@angular/core';
import { NavController } from 'ionic-angular';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage {

  page1: any = 'HomeComponent';
  page2: any = 'HomeComponent';



  constructor(public navCtrl: NavController) {
 

 
  }

}
