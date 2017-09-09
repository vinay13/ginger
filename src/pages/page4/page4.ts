import { Component,ViewChild} from '@angular/core';
import { NavController,NavParams,Events} from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { SearchResultComponent } from '../search/searchResult/search-result.component';
import { LoginPage} from '../login/login.component';
import { UploadComponent } from '../upload/upload.component';
import { CustomService } from '../../services/custom.service';
import { MasonryModule,AngularMasonry } from 'angular2-masonry';

@Component({
  selector: 'page-page4',
  templateUrl: 'page4.html'
})

export class Page4Page {
   rootNavCtrl: NavController;
   selectedIdiom = localStorage.getItem('idiom');
   lessdata;
    LessData;
    // selectedIdiom:any;
    isSelected;
    version;
    versionFlag = false;
    @ViewChild(AngularMasonry) public masonry: AngularMasonry;
   constructor(public navparams : NavParams,
                public _homeserv : HomeService,
                public cs : CustomService,
                public events : Events){
                    this.rootNavCtrl = this.navparams.get('rootNavCtrl');

                  this.events.subscribe('reloadLayout',() => {
                      //  alert('newLayout called');
                        this.newlayout();
                  });

                  this.version = localStorage.getItem('version');
                  localStorage.getItem('platform');
                  if(  this.version <  '5.0' ){
                        this.versionFlag = true;
                  }

                //  this.newselectedIdiom = this.navparams.data;
                //  this.selectedIdiom = this.newselectedIdiom.idiom;
                    this.tabcat();

                      events.subscribe('lessdata:created', (user) => {
                    console.log('Welcome', user);
                    this.lessdata = user;
                    this.LessData = this.lessdata;
                });
              
                this.lessdata = localStorage.getItem('lessdata');
                  if(this.lessdata === "false"){
                    this.LessData = false
                  } 
                  else{
                    this.LessData = true;
                }  
            }

    public tabdata;
    public tabcat(){
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.sortTabsByorder(); this.gettabdata(this.selectedIdiom,this.tabdata[2].id);  },
                                (err) => { console.log(err)},
                                () => { })
    }

    sortTabsByorder(){
      this.tabdata.sort(function(a, b){
        if ( a.order < b.order )
            return -1;
        if ( a.order > b.order )
            return 1;
        return 0;
      });
          console.log('sorted tab data',this.tabdata);
    }

    public tabIddata;
    public gifs;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ;this.textonGIFs(); this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('page3data',this.tabIddata ))
        }


  ng_class;
  click_func= "EmotionClicked" ;
  textonGIFs(){
let click_func;
      console.log('text1',this.tabIddata[0]['text']);
      console.log('text2',this.tabIddata[0].text);
    if(this.tabIddata[0].text != '' && this.tabIddata[0]['text']){
    
        this.ng_class =  'wrapper';
     
    }
    else{
      this.ng_class = 'wrapper2';

    }
  }

  doRefresh(refresher){
   this.cs.showLoader();
     this.tabcat();
   setTimeout(() => {
      this.cs.hideLoader();
      refresher.complete();
    }, 2000);
   
  }

   UploadviaWeb(){
      this.cs.showLoader();
      this.rootNavCtrl.push(UploadComponent);
      this.cs.hideLoader();
   }

    checkUserLogin(){
       let token = localStorage.getItem('access_token');
      console.log('token',token);
      if(token != null){
          this.UploadviaWeb();
      }else{
        this.rootNavCtrl.push(LoginPage);
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


     public newlayout() {
      setTimeout(() => {
            //this.masonry._msnry.layout();
      },1000);

      // console.log('AngularMasonry:', 'Layout');
    }

    ionViewWillEnter (){
      this.newlayout();
    }



            currentPage = 0;
 doInfinite(infiniteScroll) {

   this.currentPage = this.currentPage + 1;
    console.log('currentpage', this.currentPage);
    // console.log('tabId',this.tabId);
       this._homeserv.getTabDataviaTabId(this.selectedIdiom,this.tabdata[2].id,this.currentPage).subscribe(data =>
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
    }