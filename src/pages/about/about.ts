import { Component,Input, Output, EventEmitter } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage {

  
  constructor(public navCtrl: NavController) {
    // this.someEvent.emit({data: someData});
  }

}
