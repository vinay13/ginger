import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';
import { CustomService } from '../../services/custom.service';
import { UploadComponent } from '../upload/upload.component';
import { FileChooser } from '@ionic-native/file-chooser';
import { AddTagsComponent } from '../upload/add-tags/add-tags.component';
import {AppRate} from '@ionic-native/app-rate';
import { Platform } from 'ionic-angular';



@Component({
  selector: 'page-page1',
  templateUrl: 'page1.html'
})

export class Page1Page {

    rootNavCtrl: NavController;
    public selectedIdiom;
    public newselectedIdiom;
   

    constructor(public navCtrl: NavController,
                public navparams: NavParams,
                public _homeserv : HomeService,
                public cs : CustomService,
                public platform : Platform,
                public fileChooser : FileChooser){
                             
                  this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                  this.newselectedIdiom = this.navparams.data;
                  this.selectedIdiom = this.newselectedIdiom.idiom;
             
                  this.tabcat();

               

    }

    
            

    public trendingGIFs: any;
    public gifs: Array<any> = [];
  
    public tabdata;
    public tabcat(){
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.gettabdata(this.selectedIdiom,this.tabdata[0].id);  },
                                (err) => { console.log(err)},
                                () => { console.log('tabdata',this.tabdata[0].id)})
    }

    public tabIddata;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    }

    navGifDetail(url){
      this.rootNavCtrl.push(GifDetailComponent,{
        'url' : url,
        'idiom' : this.selectedIdiom
      });
    }

      UploadviaWeb(){
    this.cs.showLoader();
    this.rootNavCtrl.push(UploadComponent);
    this.cs.hideLoader();
  }

    public imageFile : any;  
  public data_response; 
  ImagePick(){
      this.fileChooser.open()
        .then(uri => {console.log(uri); this.imageFile = uri ; this.navAddTag(uri); } )
        .catch(e => console.log(e));
    }

  navAddTag(uri){
    this.cs.hideLoader();
       this.rootNavCtrl.push(AddTagsComponent,{
        'gifpath' :  uri
      });   
    }
     

}
