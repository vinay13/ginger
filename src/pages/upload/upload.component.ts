import { Component,OnInit } from '@angular/core';
import { AddTagsComponent } from './add-tags/add-tags.component'; 
import { NavController } from 'ionic-angular';
import { FileChooser } from '@ionic-native/file-chooser';
import { FilePath } from '@ionic-native/file-path';
import { AndroidPermissions } from '@ionic-native/android-permissions';
import { Diagnostic } from '@ionic-native/diagnostic';

@Component({
    selector : 'page-upload',
    templateUrl: 'upload.html'
})

export class UploadComponent implements OnInit {

    weburl;
    constructor(private navCtrl : NavController,
                private fileChooser : FileChooser,
                private filePath : FilePath,
                private androidPermissions: AndroidPermissions,
                private diagnostic: Diagnostic ){
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
        .then(uri => {console.log(uri);this.newpermissionGola(uri); } )
        .catch(e => console.log(e));
    }

    permissionGola(){
        this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.READ_EXTERNAL_STORAGE).then(
            success => {console.log('Permission granted')},
            err => this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.READ_EXTERNAL_STORAGE)
    );
// this.androidPermissions.requestPermissions([this.androidPermissions.PERMISSION.CAMERA, this.androidPermissions.PERMISSION.GET_ACCOUNTS]);
    }

     newpermissionGola(uri){
        this.diagnostic.requestExternalStorageAuthorization().then(()=>{
            //User gave permission 
             this.imageFile = uri ; 
             this.filePathfunc(uri); 
            console.log('permissionGranted');
            }).catch(error=>{
            //Handle error
        });
     }

    filePathfunc(path){
        this.filePath.resolveNativePath(path)
            .then(filePath => {console.log(filePath); this.navAddTag(filePath); })
            .catch(err => console.log(err));
    }

    // ImagePick(){
    //   let options = {
    //         maximumImagesCount: 1,
    //         quality: 100,
    //         outputType: 0
    //     };


    //     this.imagePicker.getPictures(options).then((results) => {
    //         for (var i = 0; i < results.length; i++) {
    //             console.log('Image URI: ' + results[i]);
    //         }
    //         this.navAddTag(results[0]);
    //     }, (err) => { });
    // }

//     public basePath;
//     public userImage;
//     saveImage;
//     public openCamera() {
//     this.cameraa.getPicture({
//       destinationType: this.cameraa.DestinationType.DATA_URL,
//       targetWidth : 1000,
//       targetHeight : 1000,
//       encodingType: this.cameraa.EncodingType.JPEG,
//       mediaType: this.cameraa.MediaType.PICTURE,
//       correctOrientation: true,
//       allowEdit: true,
//       quality: 30
//     }).then((imagedata) => {
//       this.basePath = 'data:image/jpeg;base64,';
//       this.userImage = imagedata;
//       this.saveImage = this.basePath+this.userImage;
//       this.navAddTag(this.saveImage);
//     },(err) => {
//     });
//   }


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