import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Trade } from '../model/trade';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TradeService {

  private tradeUrl: string;

  constructor(private http: HttpClient) {
    this.tradeUrl = environment.rest_host + '/trade';
  }
 
  public findAll(): Observable<Trade[]> {
    return this.http.get<Trade[]>(this.tradeUrl);
  }
}
