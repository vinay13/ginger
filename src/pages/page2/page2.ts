import { Component,OnInit,ViewChild } from '@angular/core';
import { NavController, NavParams ,Events } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { SearchComponent } from '../search/search.component';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';
import { IdiomComponent } from '../idiom/idiom.component';
import { LoginPage } from '../login/login.component';
import { UploadComponent } from '../upload/upload.component';
import { CustomService} from '../../services/custom.service';
import { AddTagsComponent} from '../upload/add-tags/add-tags.component';
import { FileChooser } from '@ionic-native/file-chooser';
import {AppRate} from '@ionic-native/app-rate';
import { Platform } from 'ionic-angular';
import { SearchResultComponent } from '../search/searchResult/search-result.component';

@Component({
  selector: 'page-page2',
  templateUrl: 'page2.html'
})

export class Page2Page implements OnInit{
   public tabId;
   public tabIDD;
   public selectedIdiom;
   newselectedIdiom;
   public tabIddata; 
   indexx;
   index = 0;
   dataTab;
   dataTab2;
    
    //  @ViewChild(SuperTabs) superTabs: SuperTabs;
//  appRate: any = AppRate;
    rootNavCtrl: NavController;
    public loggedIn : boolean = false;
    flag = false;
    constructor(public navCtrl: NavController, 
                public navparams: NavParams,
                public _homeserv : HomeService,
                public events : Events,
                public platform : Platform,
                public appRate: AppRate,
                public fileChooser : FileChooser,
                public cs : CustomService) {
            
                this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                this.newselectedIdiom = this.navparams.data;
                this.selectedIdiom = this.newselectedIdiom.idiom;
                this.tabIDD = this.newselectedIdiom.tabid;
                this.indexx = this.newselectedIdiom.index;
             
                console.log('dataTab',this.flag); 
                console.log( 'dataTab2',this.events);
               this.events.subscribe('tab:selected',(id) => {
                        
                             this.tabId = id;
                             this.index += 1;
                            // this.arbitpushbackdata();
                            this.gettabdata(this.selectedIdiom,id);             
                            this.events.unsubscribe('tab:selected');
                }) 
              //     console.log( 'dataTab3',this.events);

                //      this.platform.ready().then(
                //      () =>  this.appRate.preferences = {
                //        usesUntilPrompt: 3,
                //        storeAppURL: {
                //      android : 'market://details?id=com.mobigraph.xpresso'
                //    }
                //      }
                // )
        
                }
      
   public gifs =  []; 

  checkUserLogin(){
    let token = localStorage.getItem('access_token');
    console.log('token',token);
    if(token != null){
        this.UploadviaWeb();
    }else{
       this.rootNavCtrl.push(LoginPage);
    }
   
  }

  arbitpushbackdata(){
     this.gettabdata(this.selectedIdiom,localStorage.getItem('tabId'));
  }
    
  gettabdata(idiom,tabid){
   // this.cs.showLoader();
     this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ; this.textonGIFs(); this.gifs = this.tabIddata;},
                  (err) => {console.log(err);})
  }

  navGifDetail(url){
    this.rootNavCtrl.push(GifDetailComponent,{
      'url' : url,
      'idiom' : this.selectedIdiom
    });
  }

  ng_class;
  click_func= "EmotionClicked" ;
  textonGIFs(){
let click_func;
      console.log('text1',this.tabIddata[0]['text']);
      console.log('text2',this.tabIddata[0].text);
    if(this.tabIddata[0].text != '' && this.tabIddata[0]['text']){
    
        this.ng_class =  'wrapper';
      //  click_func = "EmotionClicked";
      //  console.log('click_func',click_func);
    }
    else{
      this.ng_class = 'wrapper2';
      // click_func = "GifsViewviaId(item.gifId)";
    }
  }


  EmotionClicked(tag){
      console.log('tag',tag);
      console.log('idiom',this.selectedIdiom);
      this.rootNavCtrl.push(SearchResultComponent,{
            'tag' : tag,
            'idiom': this.selectedIdiom
      });
  }

  ionViewDidEnter(){
  }

  
currentPage = 0;
 doInfinite(infiniteScroll) {

   this.currentPage = this.currentPage + 1;
    console.log('currentpage', this.currentPage);
    console.log('tabId',this.tabId);
       this._homeserv.getTabDataviaTabId(this.selectedIdiom,this.tabId,this.currentPage).subscribe(data =>
        {
          infiniteScroll.complete();
        //   this.hasMoreData = true;
        //   this.trendingGIFs = data;
          this.gifs =  this.gifs.concat(data); 
      }, 
    err => {
      infiniteScroll.complete();
      this.currentPage -= 1;
   //   this.onError(err);
    },
     () => console.log('Next Page Loading completed')
     );
  } 

  UploadviaWeb(){
    
    this.cs.showLoader();
    this.rootNavCtrl.push(UploadComponent);
    this.cs.hideLoader();
  }

  public imageFile : any;  
  public data_response; 
  ImagePick(){
 // this.appRate.promptForRating(true);
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

    public gifData;
    GifsViewviaId(tabid){
      this._homeserv.getGifviaID(tabid)
          .subscribe((res) => { this.gifData = res; this.navGifDetail(res);})
    }

    ngOnInit(){
 //   this.appRate.promptForRating(true);
      this.arbitpushbackdata();
    }
}
