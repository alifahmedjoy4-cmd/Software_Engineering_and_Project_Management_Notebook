'use strict';
/* Project calculator - talks to the servlets (/api/plan, /api/tasks, /api/history)
   and redraws the Gantt chart every time a date or setting changes. */

const $ = id => document.getElementById(id);
const DAY = 86400000;
const CELL = 18;                       // width of one calendar day in the Gantt chart (px)
const LABEL = 210;                     // width of the task-name column (px)
const MON = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
const DOW = ['SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY'];
const PALETTE = ['#1f6f8b','#3a7d44','#b5651d','#7b4f9d','#a23b5a','#4a5f8a','#8a7a1f','#2b7a78'];
const DEFAULT_START = '2026-11-08', DEFAULT_END = '2027-01-31';

const esc = s => String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const parse = s => { const [y, m, d] = s.split('-').map(Number); return Date.UTC(y, m - 1, d); };
const iso = ms => new Date(ms).toISOString().slice(0, 10);
const nice = s => { const t = new Date(parse(s)); return t.getUTCDate() + ' ' + MON[t.getUTCMonth()] + ' ' + t.getUTCFullYear(); };

/* ---------- controls ---------- */
function buildOffDays() {
  $('offDays').innerHTML = DOW.map(d => {
    const checked = (d === 'FRIDAY' || d === 'SATURDAY') ? 'checked' : '';
    return `<label><input type="checkbox" class="off" value="${d}" ${checked}>${d.slice(0, 3)[0] + d.slice(1, 3).toLowerCase()}</label>`;
  }).join('');
}

function query() {
  const off = [...document.querySelectorAll('.off:checked')].map(c => c.value).join(',');
  return new URLSearchParams({
    start: $('start').value, end: $('end').value, asOf: $('asOf').value,
    offDays: off, holidays: $('holidays').value.trim()
  });
}

function showError(msg) { $('error').textContent = msg; $('error').hidden = !msg; }
function showNotice(msg) { $('notice').textContent = msg; $('notice').hidden = !msg; }

/* ---------- calculate + draw ---------- */
let timer;
function schedule() { clearTimeout(timer); timer = setTimeout(calculate, 250); }

async function calculate() {
  showNotice('');
  try {
    const res = await fetch('api/plan?' + query());
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Request failed');
    showError('');
    render(data);
  } catch (e) {
    showError(e.message);
  }
}

function render(d) {
  renderStats(d);
  renderProgress(d);
  renderGantt(d);
  renderTaskTable(d);
  renderDaily(d);
}

function renderStats(d) {
  const items = [
    [d.totalCalendarDays, 'calendar days'], [d.totalWorkDays, 'working days'],
    [d.totalHours + ' h', 'total work'], [d.hoursPerDay + ' h', 'work each day']
  ];
  $('stats').innerHTML = items.map(([v, l]) => `<div class="stat"><b>${v}</b><span>${l}</span></div>`).join('');
}

function renderProgress(d) {
  const p = d.progress;
  $('progress').hidden = !p;
  if (!p) return;
  $('progressMsg').textContent = p.message;
  $('meterFill').style.width = p.percentDone + '%';
}

function renderGantt(d) {
  const s = parse(d.startDate), e = parse(d.endDate);
  const n = Math.round((e - s) / DAY) + 1;
  const off = new Set(d.offDays), hol = new Set(d.holidays);
  const phases = [...new Set(d.tasks.map(t => t.phase))];
  const color = ph => PALETTE[phases.indexOf(ph) % PALETTE.length];
  const isOff = i => { const t = s + i * DAY; return off.has(DOW[new Date(t).getUTCDay()]) || hol.has(iso(t)); };
  const idx = date => Math.round((parse(date) - s) / DAY);

  $('legend').innerHTML = phases.map(p => `<span><i style="background:${color(p)}"></i>${esc(p)}</span>`).join('');

  let months = '';
  for (let i = 0; i < n;) {
    const dt = new Date(s + i * DAY), m = dt.getUTCMonth();
    let j = i;
    while (j < n && new Date(s + j * DAY).getUTCMonth() === m) j++;
    months += `<div class="g-month" style="left:${i * CELL}px;width:${(j - i) * CELL}px">${MON[m]} ${dt.getUTCFullYear()}</div>`;
    i = j;
  }
  let dayCells = '', plain = '';
  for (let i = 0; i < n; i++) {
    const dt = new Date(s + i * DAY), o = isOff(i) ? ' off' : '';
    dayCells += `<div class="g-cell${o}${dt.getUTCDate() === 1 ? ' first' : ''}">${dt.getUTCDate()}</div>`;
    plain += `<div class="g-cell${o}"></div>`;
  }

  let html = `<div class="g" style="--n:${n};--w:${CELL}px">`;
  html += `<div class="g-row head"><div class="g-label">Task</div><div class="g-track">${months}</div></div>`;
  html += `<div class="g-row head"><div class="g-label"></div><div class="g-track">${dayCells}</div></div>`;
  for (const t of d.tasks) {
    const left = idx(t.startDate) * CELL, width = (idx(t.endDate) - idx(t.startDate) + 1) * CELL;
    const tip = `${t.name}\n${nice(t.startDate)} to ${nice(t.endDate)}\n${t.workDays} working days, ${t.hours} h`;
    html += `<div class="g-row"><div class="g-label" title="${esc(t.name)}"><i style="background:${color(t.phase)}"></i>${esc(t.name)}</div>
      <div class="g-track">${plain}
        <div class="g-bar" style="left:${left}px;width:${width}px;background:${color(t.phase)}" title="${esc(tip)}">
          <div class="fill" style="width:${t.progressPercent}%"></div>${width > 70 ? '<span>' + t.hours + ' h</span>' : ''}
        </div></div></div>`;
  }
  if (d.progress) {
    const k = idx(d.progress.asOf);
    if (k >= 0 && k < n) {
      const x = LABEL + (k + 0.5) * CELL;
      html += `<div class="g-line" style="left:${x}px"></div><div class="g-flag" style="left:${x}px">${nice(d.progress.asOf)}</div>`;
    }
  }
  $('gantt').innerHTML = html + '</div>';
}

function renderTaskTable(d) {
  const head = '<tr><th>Phase</th><th>Task</th><th>Start</th><th>End</th><th class="num">Working days</th><th class="num">Hours</th><th class="num">Share</th>' +
               (d.progress ? '<th class="num">Done</th>' : '') + '</tr>';
  const rows = d.tasks.map(t => `<tr><td>${esc(t.phase)}</td><td>${esc(t.name)}</td><td>${nice(t.startDate)}</td><td>${nice(t.endDate)}</td>
    <td class="num">${t.workDays}</td><td class="num">${t.hours}</td><td class="num">${t.percentOfProject}%</td>
    ${d.progress ? `<td class="num">${t.progressPercent}%</td>` : ''}</tr>`).join('');
  $('taskTable').innerHTML = head + rows;
}

function renderDaily(d) {
  const asOf = d.progress ? d.progress.asOf : null;
  const head = '<tr><th>Date</th><th>Day</th><th>Work to do</th><th class="num">Hours</th><th class="num">Project done</th></tr>';
  const rows = d.daily.map(x => `<tr class="${x.date === asOf ? 'check' : ''}"><td>${nice(x.date)}</td><td>${x.weekday}</td>
    <td>${x.items.map(i => `${esc(i.task)} (${i.hours} h)`).join('<br>')}</td>
    <td class="num">${x.hours}</td><td class="num">${x.cumulativePercent}%</td></tr>`).join('');
  $('dailyTable').innerHTML = head + rows;
}

/* ---------- save + history ---------- */
async function savePlan() {
  try {
    const res = await fetch('api/plan', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: query().toString()
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Could not save');
    showError('');
    showNotice('Plan #' + data.id + ' saved.');
    loadHistory();
  } catch (e) { showError(e.message); }
}

async function loadHistory() {
  const res = await fetch('api/history');
  const list = await res.json();
  if (!res.ok || !list.length) { $('history').innerHTML = '<tr><td class="empty">No saved plans yet. Use "Save this plan".</td></tr>'; return; }
  $('history').innerHTML = '<tr><th>#</th><th>Start</th><th>Last date</th><th class="num">Working days</th><th class="num">Hours</th><th>Saved at</th><th></th></tr>' +
    list.map(p => `<tr><td>${p.id}</td><td>${nice(p.startDate)}</td><td>${nice(p.endDate)}</td>
      <td class="num">${p.totalWorkDays}</td><td class="num">${p.totalHours}</td><td>${esc(p.createdAt.slice(0, 16))}</td>
      <td><button data-load="${p.id}">Use these dates</button></td></tr>`).join('');
  $('history').querySelectorAll('[data-load]').forEach(b => b.onclick = () => {
    const p = list.find(x => x.id == b.dataset.load);
    $('start').value = p.startDate; $('end').value = p.endDate; $('holidays').value = p.holidays;
    document.querySelectorAll('.off').forEach(c => c.checked = p.offDays.split(',').includes(c.value));
    calculate();
  });
}

/* ---------- task manager ---------- */
async function loadTasks() {
  const res = await fetch('api/tasks');
  const list = await res.json();
  if (!res.ok) { showError(list.error); return; }
  $('taskList').innerHTML = '<tr><th>Phase</th><th>Task</th><th class="num">Hours</th><th></th></tr>' +
    list.map(t => `<tr><td>${esc(t.phase)}</td><td>${esc(t.name)}</td><td class="num">${t.hours}</td>
      <td><button data-del="${t.id}">Delete</button></td></tr>`).join('');
  $('taskList').querySelectorAll('[data-del]').forEach(b => b.onclick = async () => {
    await postTask(new URLSearchParams({ action: 'delete', id: b.dataset.del }));
  });
}

async function postTask(params) {
  const res = await fetch('api/tasks', {
    method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: params.toString()
  });
  const data = await res.json();
  if (!res.ok) { showError(data.error); return; }
  showError('');
  loadTasks();
  calculate();
}

/* ---------- start ---------- */
buildOffDays();
['start', 'end', 'asOf', 'holidays'].forEach(id => $(id).addEventListener('input', schedule));
$('offDays').addEventListener('change', schedule);
$('saveBtn').onclick = savePlan;
$('resetBtn').onclick = () => { $('start').value = DEFAULT_START; $('end').value = DEFAULT_END; $('asOf').value = ''; calculate(); };
$('taskForm').addEventListener('submit', e => {
  e.preventDefault();
  const f = new FormData(e.target);
  postTask(new URLSearchParams({ action: 'add', phase: f.get('phase'), name: f.get('name'), hours: f.get('hours') }));
  e.target.reset();
});
calculate();
loadTasks();
loadHistory();
