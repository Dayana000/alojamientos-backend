// ==========================================
// reservas.service.ts
// ==========================================
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ReservaRequest } from '../models/reserva-request.model';
import { Reserva } from '../models/reserva.model';
import { Page } from './alojamientos.service';

@Injectable({
  providedIn: 'root',
})
export class ReservasService {

  private apiUrl = `${environment.apiBase}/reservas`;

  constructor(private http: HttpClient) {}

  // ==========================================
  // CREATE - Crear reserva
  // ==========================================
  crearReserva(payload: ReservaRequest): Observable<Reserva> {
    return this.http.post<Reserva>(this.apiUrl, payload);
  }

  // ==========================================
  // GET - Listar reservas por usuario
  // ==========================================
  listarPorUsuario(
    usuarioId: number,
    page: number = 0,
    size: number = 10
  ): Observable<Page<Reserva>> {
    return this.http.get<Page<Reserva>>(this.apiUrl, {
      params: {
        usuarioId,
        page,
        size
      }
    });
  }

  // ==========================================
  // GET - Listar reservas por alojamiento
  // ==========================================
  listarPorAlojamiento(
    alojamientoId: number,
    page: number = 0,
    size: number = 10
  ): Observable<Page<Reserva>> {
    return this.http.get<Page<Reserva>>(this.apiUrl, {
      params: {
        alojamientoId,
        page,
        size
      }
    });
  }

  // ==========================================
  // PUT - Cancelar reserva
  // ==========================================
  cancelarReserva(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/cancelar`, {});
  }

  obtenerReservasPorUsuario(usuarioId: number) {
    return this.http.get<any[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

}
