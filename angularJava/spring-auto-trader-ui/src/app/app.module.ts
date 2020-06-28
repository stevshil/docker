import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { TradeListComponent } from './trade-list/trade-list.component';
import { TradeService } from './service/trade.service';

@NgModule({
  declarations: [
    AppComponent,
    TradeListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [TradeService],
  bootstrap: [AppComponent]
})
export class AppModule { }
