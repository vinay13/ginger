import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { IonicPage, NavController,NavParams , Events , ModalController } from 'ionic-angular';
import { Page1Page } from "../page1/page1";
import { Page2Page } from "../page2/page2";
import { Page3Page } from "../page3/page3";
import { Page4Page } from "../page4/page4";
import { HomeService } from '../../services/home.service';
import { IdiomComponent } from '../idiom/idiom.component';
import { SearchComponent } from '../search/search.component';
import { LoginPage } from '../login/login.component';
import { ProfileComponent } from '../profile/profile.component';

//import { SuperTabsController } from 'ionic2-super-tabs';
//import { SuperTabsController } from '../../ionic2-super-tabs/src';

// @IonicPage({
//   segment: 'about/:type'
// }) 



@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage implements OnInit {

  public page1: any = Page1Page;
  public page2: any = Page2Page;
  public page3: any = Page3Page;
  public page4: any = Page4Page;

  showIcons: boolean = false;
  showTitles: boolean = true;
  pageTitle: string = 'abc';
  title;
  public lang;
  public tabdata;
  public trendingGIFs: any;
  public gifs: Array<any> = [];
  public selectedIdiom;
   idiom3;
idiomdict;
// @Output()
// tabSelect: EventEmitter<any> = new EventEmitter<any>();
//  onTabSelect(index: number) {
//     this.tabSelect.emit(index);
//   }
  tabIndex;

  colors =  ["secondary","danger","primary","favcolor1","favcolor2"];
  constructor(public navCtrl: NavController, 
              private navParams: NavParams,
              public _homeserv : HomeService,
              public events: Events,
              public modalCtrl : ModalController) { 
                               
                  this.selectedIdiom = this.navParams.get('idiom') || localStorage.getItem('idiom');
                ;
                  console.log(localStorage.getItem('tabIndex'));
              
              if( localStorage.getItem('tabIndex') != null ){
                this.tabIndex = localStorage.getItem('tabIndex');
              }
              else{
                  this.tabIndex = "0";
                  alert(this.tabIndex);
              }
                 

                  //testing
                    this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.tabdata.splice(0,2);this.abcetc(this.tabdata[0].id);this.tabsLoaded = true;    },
                    (err) => { console.log(err)},
                    () => {})

                  //end here
                  console.log('aboutpageIdiom',this.selectedIdiom);
                 // this.events.publish('idiom:selected',  this.selectedIdiom);
              //   this.tabsCategories();
  const type = navParams.get('type');
    switch (type) {
      case 'icons-only':
        this.showTitles = false;
        this.pageTitle += ' - Icons only';
        break;

      case 'titles-only':
        this.showIcons = true;
        this.pageTitle += ' - Titles only';
        break;
    }
  }

  tabsLoaded =  false;

  ngOnInit(){
    this.lang =  "assets/icon/ic_"+ this.selectedIdiom +".png";
  

  }

  abcetc(tbid){
    console.log('abctabid',tbid);
       this.idiomdict = {
                      'idiom': this.selectedIdiom,
                      'index' : 0,
                      'tabid' : tbid
                   }
     this.idiom3 = this.idiomdict;
     
   //this.gettabdata(this.selectedIdiom,tbid);           
  }

  arbittt(ftdataa){
  
  }
 

  checklogin(){
    if(localStorage.getItem("access_token") === null){
     this.navCtrl.push(LoginPage,{
        'idiom': this.selectedIdiom
    });  
  }

  else{
      this.navCtrl.push(ProfileComponent,{
        'idiom': this.selectedIdiom
    });
  }
    
  

  }

  ngAfterViewInit() {
      
    // this.superTabsCtrl.increaseBadge('page1', 10);
    // this.superTabsCtrl.enableTabSwipe('page3', false);
    // this.superTabsCtrl.enableTabsSwipe(false);
  }

   slideTo(tabsId){
     console.log('in slide to fun',tabsId);
   }
  
  toolbarColor = "primary";
  count = 0 ;
  onTabSelect(ev : any){
     this.events.publish('tab:selected', ev.id);
     localStorage.setItem('tabIndex',ev.index);
     localStorage.setItem('tabId',ev.id);
     
        this.toolbarColor = this.colors[Math.floor(Math.random() * this.colors.length)];  
       // this.count += 1; 
     
     
     console.log(ev);
  }

  public tabIddata;
  gettabdata(idiom,tabid){
     this.tabIddata = [];
     this._homeserv.getTabDataviaTabId(idiom,tabid,0)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
}

  searchButton(){
      //   let modal = this.modalCtrl.create(SearchComponent);
      // modal.present();
      this.navCtrl.push(SearchComponent,{
        'idiom':this.selectedIdiom
      });
  }

    dismiss(){
      this.dismiss();
    }

  navIdiom(){
     this.navCtrl.push(IdiomComponent);
  }



  // ngOnInit(): void {
    
    
  // }
}