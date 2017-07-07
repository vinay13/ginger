import { Component,OnInit } from '@angular/core';
import { AddTagsComponent } from './add-tags/add-tags.component'; 
import { NavController } from 'ionic-angular';

@Component({
    selector : 'page-upload',
    templateUrl: 'upload.html'
})

export class UploadComponent implements OnInit {

   
    weburl;
    constructor(private navCtrl : NavController ){
      //  this.selectedIdiom = this.navparmas.get();
    }

    AddTags(weburl){
        console.log('upload click1');
        this.navCtrl.push(AddTagsComponent,{
            'weburl' : weburl 
        });
    }

    ngOnInit(){}

}