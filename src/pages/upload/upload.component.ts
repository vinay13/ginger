import { Component } from '@angular/core';
import { AddTagsComponent } from './add-tags/add-tags.component'; 
import { NavController } from 'ionic-angular';

@Component({
    selector : 'page-upload',
    templateUrl: 'upload.html'
})

export class UploadComponent {

    constructor(private navCtrl : NavController ){}

    

    AddTags(){
        console.log('upload click1');
        this.navCtrl.push(AddTagsComponent);
    }

}