import { Component,OnInit } from '@angular/core';
import { NavController, NavParams ,Events } from 'ionic-angular';
import { HomeService } from '../../services/home.service';

@Component({
  selector: 'page-page2',
  templateUrl: 'page2.html'
})
export class Page2Page implements OnInit{
    // celebrity page
   public tabId;
   public selectedIdiom = "Hindi";
     // public tabId = 1498144144545;
    constructor(public navCtrl: NavController, 
                public navParams: NavParams,
                public _homeserv : HomeService,
                public events : Events) {
                
                this.events.subscribe('tab:selected',(id) => {
                    this.tabId = id;
                    this.getTabdata(this.selectedIdiom,this.tabId);
                   console.log('tabId',this.tabId);
                 });

                  
                
                  
                }
                
    public trendingGIFs: any;
    public gifs: Array<any> = [];
    
   
    public tabIddata; 
    getTabdata(idiom,tabId){
      console.log('tabbb3',this.tabId);
      this._homeserv.getTabDataviaTabId(idiom,tabId)
      .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata},
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    }

    ngOnInit(){
      
    }
}
