# Customer実装記録

## 実装したもの

- Entity
- Repository
- Service
- Controller

## API

GET    /api/customers
GET    /api/customers/{id}
POST   /api/customers
PUT    /api/customers/{id}
DELETE /api/customers/{id}

## 実装で分かったこと

- EntityはDBとの対応を担当
- RepositoryはDB操作を担当
- Serviceは業務処理を担当
- ControllerはHTTPリクエストを担当

## 次のProjectで再利用するもの

- Controller → Service → Repositoryの構造
- UUIDによるID管理
- CRUD APIの命名規則
- Serviceの責務分離

## Customer固有のもの

- customerName
- customerKanaName
- phoneNumber
- gender