import { BrowserRouter as Router, Routes, Route, Link, useLocation } from 'react-router-dom'
import { Button } from '@/components/ui/button.jsx'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card.jsx'
import { Users, BookOpen, UserCheck, Home } from 'lucide-react'
import PessoasPage from './pages/PessoasPage.jsx'
import CursosPage from './pages/CursosPage.jsx'
import MatriculasPage from './pages/MatriculasPage.jsx'
import HomePage from './pages/HomePage.jsx'
import './App.css'

function Navigation() {
  const location = useLocation()
  
  const navItems = [
    { path: '/', label: 'Início', icon: Home },
    { path: '/pessoas', label: 'Pessoas', icon: Users },
    { path: '/cursos', label: 'Cursos', icon: BookOpen },
    { path: '/matriculas', label: 'Matrículas', icon: UserCheck },
  ]

  return (
    <nav className="bg-white shadow-sm border-b">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <h1 className="text-xl font-bold text-gray-900">Espaço La Música</h1>
            </div>
            <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
              {navItems.map((item) => {
                const Icon = item.icon
                const isActive = location.pathname === item.path
                return (
                  <Link
                    key={item.path}
                    to={item.path}
                    className={`inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium ${
                      isActive
                        ? 'border-blue-500 text-gray-900'
                        : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
                    }`}
                  >
                    <Icon className="w-4 h-4 mr-2" />
                    {item.label}
                  </Link>
                )
              })}
            </div>
          </div>
        </div>
      </div>
    </nav>
  )
}

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Navigation />
        <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/pessoas" element={<PessoasPage />} />
            <Route path="/cursos" element={<CursosPage />} />
            <Route path="/matriculas" element={<MatriculasPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App

