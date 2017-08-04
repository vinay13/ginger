import { Component } from '@angular/core';
import { NavController, NavParams,Events } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { SearchResultComponent } from '../search/searchResult/search-result.component';
import { LoginPage} from '../login/login.component';
import { UploadComponent } from '../upload/upload.component';
import { CustomService } from '../../services/custom.service';

@Component({
  selector: 'page-page3',
  templateUrl: 'page3.html'
})

export class Page3Page {
   rootNavCtrl: NavController;
    public selectedIdiom;
    public newselectedIdiom;
    lessdata;
    LessData;
 // selectedIdiom:any;
  constructor(public navCtrl: NavController,
               public navParams: NavParams,
               public _homeserv : HomeService,
               public cs : CustomService,
               public events : Events)
              { 
                   this.rootNavCtrl = this.navParams.get('rootNavCtrl');
                  this.newselectedIdiom = this.navParams.data;
                  this.selectedIdiom = this.newselectedIdiom.idiom;
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
                    .subscribe( (res) => { this.tabdata = res.tabs; this.gettabdata(this.selectedIdiom,this.tabdata[1].id); },
                                (err) => { console.log(err);},
                                () => { console.log('tabdata',this.tabdata[2].id)})
    }

    public tabIddata;
    public gifs;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
      
       this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ;this.textonGIFs();  this.gifs = this.tabIddata; },
                  (err) => {console.log(err);},
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

    EmotionClicked(tag){
      console.log('tag',tag);
      console.log('idiom',this.selectedIdiom);
      this.rootNavCtrl.push(SearchResultComponent,{
            'tag' : tag,
            'idiom': this.selectedIdiom
      });
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
}
