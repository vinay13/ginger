import { Component,Input, Output, EventEmitter } from '@angular/core';
import { IonicPage, NavController,NavParams } from 'ionic-angular';
import { Page1Page } from "../page1/page1";
import { Page2Page } from "../page2/page2";
import { Page3Page } from "../page3/page3";
import { HomeService } from '../../services/home.service';
// import { SuperTabsController } from 'ionic2-super-tabs';
// import { SuperTabsController } from '../../ionic2-super-tabs/src';

// @IonicPage({
//   segment: 'about/:type'
// }) 

@Component({
  selector: 'page-about',
  templateUrl: 'about.html'
})

export class AboutPage {

  public page1: any = Page1Page;
  public page2: any = Page2Page;
  public page3: any = Page3Page;
  

  showIcons: boolean = false;
  showTitles: boolean = true;
  pageTitle: string = 'abc';


  constructor(public navCtrl: NavController,  private navParams: NavParams,
              public _homeserv : HomeService) {
             //     this.getTrendingGIFs();
 const type = navParams.get('type');
    switch (type) {
      case 'icons-only':
        this.showTitles = false;
        this.pageTitle += ' - Icons only';
        break;

      case 'titles-only':
        this.showIcons = false;
        this.pageTitle += ' - Titles only';
        break;
    }
  }


  ngAfterViewInit() {
    // this.superTabsCtrl.increaseBadge('page1', 10);
    // this.superTabsCtrl.enableTabSwipe('page3', false);
    // this.superTabsCtrl.enableTabsSwipe(false);
  }

  onTabSelect(tab: { index: number; id: string; }){
      console.log(`Selected tab: `, tab);
  }

    public trendingGIFs: any;
    public gifs: Array<any> = [];
    public selectedIdiom = "hindi"; 
    // getTrendingGIFs(){
    // this._homeserv.getTrendingGifs(this.selectedIdiom)
    // .subscribe( (result) => { this.trendingGIFs = result ; this.gifs = this.gifs.concat(this.trendingGIFs.data);},
    //             (err) => {  console.log(err); },
    //             () => console.log('trendingGifs',this.trendingGIFs))
    // }

}