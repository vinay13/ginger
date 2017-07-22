import { Component,OnInit } from '@angular/core';
import { AddTagsComponent } from './add-tags/add-tags.component'; 
import { NavController } from 'ionic-angular';
import { FileChooser } from '@ionic-native/file-chooser';
import { Camera,CameraOptions } from '@ionic-native/camera';
import { ImagePicker } from '@ionic-native/image-picker';

@Component({
    selector : 'page-upload',
    templateUrl: 'upload.html'
})

export class UploadComponent implements OnInit {

    weburl;
    constructor(private navCtrl : NavController,
                private fileChooser : FileChooser,
                public cameraa : Camera,
                private imagePicker: ImagePicker ){
      //  this.selectedIdiom = this.navparmas.get();
    }

    AddTags(weburl){
        console.log('upload click1');
        this.navCtrl.push(AddTagsComponent,{
            'weburl' : weburl 
        });
    }
    // public imageFile : any;  
    // public data_response; 
    // ImagePick(){
    //   this.fileChooser.open()
    //     .then(uri => {console.log(uri); this.imageFile = uri ; this.navAddTag(uri); } )
    //     .catch(e => console.log(e));
    // }

    ImagePick(){
      let options = {
            maximumImagesCount: 1,
            quality: 100,
            outputType: 0
        };


        this.imagePicker.getPictures(options).then((results) => {
            for (var i = 0; i < results.length; i++) {
                console.log('Image URI: ' + results[i]);
            }
            this.navAddTag(results[0]);
        }, (err) => { });
    }

    // base64Image;
    // ImageFile;
    // ImagePick(){
    //     this.cameraa.getPicture({
    //         destinationType: this.cameraa.DestinationType.DATA_URL,
    //         sourceType: this.cameraa.PictureSourceType.PHOTOLIBRARY
    //     }).then((imagedata)=>{
    //     this.base64Image = 'data:image/gif;base64,' + imagedata;
    //     this.ImageFile = imagedata ; 
    //     this.navAddTag( this.base64Image);
    //     },(err)=>{
    //     console.log(err);
    //     });
    // }

    navAddTag(uri){
        console.log('uri',uri);
       this.navCtrl.push(AddTagsComponent,{
        'gifpath' :  uri,
      });   
    }

    ngOnInit(){}
}