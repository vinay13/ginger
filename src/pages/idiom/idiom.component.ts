import { Component } from '@angular/core';
import { NavController,NavParams, Nav } from 'ionic-angular'; 
import { HomeComponent } from '../home/home.component'; 
import { AboutPage } from '../about/about.ts';
import { Page2Page} from '../page2/page2.ts';
 
@Component({
     selector : 'page-idiom',
     templateUrl : 'idiom.html'
})

export class IdiomComponent{
 
    public selectedHero : string = "white";
     constructor( private navCtrl : NavController ,
                  private navparams : NavParams,
                  public nav : Nav){
        //  this.selectedIdiom();
    }

    public idioms = [
                        {"id":1 , "name": "Hindi"},
                        {"id":2 , "name": "Tamil"},
                        {"id":3 , "name": "Telugu"},
                        {"id":4 , "name": "Kannada"},
                        {"id":5 , "name": "Malayalam"}
                    ];

     public selectedIndex: number = -1;
     public bgcolor : string ;
     public showbutton = true;

    selectedIdiom(index){
          this.selectedIndex = index;
          this.showbutton = false;
          console.log('index',index);    
    }

    storeIdiom(){
        localStorage.setItem('idiom',this.idioms[this.selectedIndex].name);
    }

    navHomePage(){
         console.log('idiom',this.idioms[this.selectedIndex].name);
         this.storeIdiom();
        //  this.nav.(AboutPage);
         this.nav.setRoot(AboutPage,{
             'idiom': this.idioms[this.selectedIndex].name 
         });
    }

 }