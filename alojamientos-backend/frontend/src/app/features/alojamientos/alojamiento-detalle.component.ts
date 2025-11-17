import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AlojamientosService } from '../../core/services/alojamientos.service';
import { ReservasService } from '../../core/services/reservas.service';
import { Alojamiento } from '../../core/models/alojamiento.model';
import { ReservaRequest } from '../../core/models/reserva-request.model';

@Component({
  selector: 'app-alojamiento-detalle',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule // ⬅️ NECESARIO PARA routerLink
  ],
  templateUrl: './alojamiento-detalle.html',
  styleUrls: ['./alojamiento-detalle.scss'],
})
export class AlojamientoDetalleComponent implements OnInit {

  alojamiento?: Alojamiento;
  loading = false;
  error = '';

  reservaForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private alojamientosService: AlojamientosService,
    private reservasService: ReservasService
  ) {}

  ngOnInit(): void {
    // Formulario de reserva
    this.reservaForm = this.fb.group({
      checkIn: ['', Validators.required],
      checkOut: ['', Validators.required],
      numeroHuespedes: [1, [Validators.required, Validators.min(1)]]
    });

    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || isNaN(id)) {
      this.error = 'ID de alojamiento inválido.';
      return;
    }

    this.cargarAlojamiento(id);
  }

  cargarAlojamiento(id: number) {
    this.loading = true;

    this.alojamientosService.obtenerPorId(id).subscribe({
      next: (data) => {
        this.alojamiento = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'No se pudo cargar el alojamiento.';
        this.loading = false;
      },
    });
  }

  reservar() {
    if (!this.reservaForm.valid || !this.alojamiento) {
      alert('Completa todos los datos de la reserva');
      return;
    }

    // Verificar que el usuario esté logueado
    const usuario = JSON.parse(localStorage.getItem('user') || '{}');

    if (!usuario?.id) {
      alert('Debes iniciar sesión para reservar.');
      this.router.navigate(['/auth/login']);
      return;
    }

    // Crear payload compatible con el backend
    const payload: ReservaRequest = {
      usuarioId: usuario.id,
      alojamientoId: this.alojamiento.id!,
      checkIn: this.reservaForm.value.checkIn,
      checkOut: this.reservaForm.value.checkOut,
      huespedes: this.reservaForm.value.huespedes
    };

    this.reservasService.crearReserva(payload).subscribe({
      next: (resp) => {
        alert('Reserva creada exitosamente ✔️');
        console.log(resp);
      },
      error: (err) => {
        console.error(err);
        alert('Error al crear reserva: ' + (err.error?.detail || err.message));
      }
    });
  }
}
