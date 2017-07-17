import { Component,OnInit } from '@angular/core';
import { AddTagsComponent } from './add-tags/add-tags.component'; 
import { NavController } from 'ionic-angular';
import { FileChooser } from '@ionic-native/file-chooser';

@Component({
    selector : 'page-upload',
    templateUrl: 'upload.html'
})

export class UploadComponent implements OnInit {

    weburl;
    constructor(private navCtrl : NavController,
                private fileChooser : FileChooser ){
      //  this.selectedIdiom = this.navparmas.get();
    }

    AddTags(weburl){
        console.log('upload click1');
        this.navCtrl.push(AddTagsComponent,{
            'weburl' : weburl 
        });
    }

    public imageFile : any;  
    public data_response; 
    ImagePick(){
      this.fileChooser.open()
        .then(uri => {console.log(uri); this.imageFile = uri ; this.navAddTag(uri); } )
        .catch(e => console.log(e));
    }

    navAddTag(uri){
       this.navCtrl.push(AddTagsComponent,{
        'gifpath' :  uri
      });   
    }

    ngOnInit(){}

}