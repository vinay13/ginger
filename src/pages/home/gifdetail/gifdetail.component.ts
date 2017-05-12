import { Component } from '@angular/core';
import { PopoverController } from 'ionic-angular';
import { PopOverComponent } from './popover';
import { SearchResultComponent } from '../../search/searchResult/search-result.component';
import { NavController } from 'ionic-angular';

@Component({
    selector : 'page-gifdetail',
    templateUrl : 'gifdetail.html'
})


export class GifDetailComponent {

    constructor(public popoverCtrl : PopoverController,
                public navCtrl : NavController){}


    presentPopover(){
        let popover = this.popoverCtrl.create(PopOverComponent);
        popover.present();
    }

    TagClicked(tag){
        this.navCtrl.push(SearchResultComponent,{
            'tag' : tag
        });
    }
    
}