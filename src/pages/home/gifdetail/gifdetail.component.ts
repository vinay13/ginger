import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})



export class GifDetailComponent {

    constructor(public popoverCtrl : PopoverController){}

    presentPopover(){
        let popover = this.popoverCtrl.create(PopOverComponent);
        popover.present();
    }
    
}