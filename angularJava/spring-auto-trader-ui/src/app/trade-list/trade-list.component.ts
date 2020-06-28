import { Observable, timer, Subscription } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { Trade } from '../model/trade';
import { TradeService } from '../service/trade.service';

@Component({
  selector: 'app-trade-list',
  templateUrl: './trade-list.component.html',
  styleUrls: ['./trade-list.component.css']
})
export class TradeListComponent implements OnInit {

  private updateSubscription: Subscription;

  trades: Trade[];

  constructor(private tradeService: TradeService) { }

  ngOnInit() {
    this.updateTrades();
  }

  ngOnDestroy() {
      this.updateSubscription.unsubscribe();
  }


  private updateTrades() {
  this.tradeService.findAll().subscribe(data => {
      this.trades = data.reverse();
      console.log("loading trades: ");
      console.log(this.trades);
      this.startRefreshTimer();
    });
  }

  private startRefreshTimer() {
    this.updateSubscription = timer(10000).subscribe(
      (val) => { this.updateTrades() }
    );
  }
}
